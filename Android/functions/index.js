const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.rankingPessoa = functions.https.onCall( async (data, _context) => {

    const email = data['email'];
    var Index = 1;

    return await admin.firestore().collection('usuarios').orderBy('pontosTotais', 'desc').get().then(snapshot => {
        const TodosValores = Object.values(snapshot.docs);
        const TodosChaves = Object.keys(snapshot.docs);

        for (let i = 0; i < snapshot.docs.length; i++) {
            const Pessoa = TodosValores[i];
            const DadosPessoa = Pessoa.data();
            if(DadosPessoa['email'] === email){
                    break;
            } else {
                    Index += 1;
            }
        }
        return Index;
    })
    .catch(erro => {
        console.log(erro);
        return Index;
    });
});

exports.adicionarFila = functions.https.onCall( async (data, _context) => {

    const DocumentoPassado = data['id'];
    const ValorPassado = data['valor'];
    const HorarioPassado = data['horario'];
    const TipoFila = data['tipoFila'];

    try {

        const valores =  await admin.firestore().doc('notasUsuarios/' + DocumentoPassado).get();

        //Recuperar Documento
        const Dados = valores.data();
        const FilaMudanca = Dados[TipoFila];

        const ValorChaves = Object.keys(FilaMudanca);
        const ValorValues = Object.values(FilaMudanca);
        const ValorEntidades = Object.entries(FilaMudanca);

        if(FilaMudanca.hasOwnProperty(HorarioPassado)){
            const ListaNova = new Array();
            FilaMudanca[HorarioPassado].forEach(element => {
                ListaNova.push(element);
            });
            ListaNova.push(parseFloat(ValorPassado));
            Object.assign(FilaMudanca[HorarioPassado], ListaNova);
        } else {
            var Lista = new Array();
            Lista.push(parseFloat(ValorPassado));
            Object.assign(FilaMudanca, { [HorarioPassado] : Lista});
        }

        console.log(Dados);

        var Tamanho = 0;
        var Media = 0;

        try {
            Tamanho = FilaMudanca[HorarioPassado].length;
            FilaMudanca[HorarioPassado].forEach((valores) => {
                Media += valores;
            });
        } catch (error) {
            console.log(error);
        }

        var MediaFinal = Media / Tamanho;

        admin.firestore().collection('lojas/').doc(DocumentoPassado).get()
        .then(snapshotDoc =>{
            const DadosMedia = snapshotDoc.data();

            const FilaMedia = DadosMedia[TipoFila];
            const ValorNovo = Object.values(FilaMedia);

            Object.assign(FilaMedia, { [HorarioPassado]: MediaFinal});

            var Quantidade = DadosMedia['quantidadeAvaliacoesFila'];
            DadosMedia['quantidadeAvaliacoesFila'] = Quantidade + 1;

            admin.firestore().collection('lojas/').doc(DocumentoPassado).set(DadosMedia);

            return "ok";

        })
        .catch (error =>{
            console.log(error);
            return "Erro ao mandar a nota";
        });

        admin.firestore().collection('notasUsuarios/').doc(DocumentoPassado).set(Dados);

        return "Fila Atualizado";

    }
    catch(error) {
        console.log(error);
        return "Erro ao atualizar lista";
    }
});

exports.adicionarNotaLocal = functions.https.onCall(async (data, _context) =>{

    const email = data["email"];
    const valor = data["valor"];
    const loja = data["loja"];

    const Loja = await admin.firestore().collection('lojas/').doc(loja).get();

    await admin.firestore().doc('notasUsuarios/' + loja).get().then(snapshot =>{

        const Dados = snapshot.data()
        const notas = Dados["notasRanking"];

        var NotasChaves = Object.keys(notas);
        var NotasValues = Object.values(notas);

        if(NotasChaves.hasOwnProperty(email)){
            notas[email] = valor;
        } else {
            Object.assign(notas, { [email] : valor});
        }

        var Notas = 0;
        NotasChaves = Object.keys(notas);
        NotasValues = Object.values(notas);
        const Tamanho = NotasChaves.length;

        NotasValues.forEach(element =>{
            Notas += element;
        });

        const MediaFinal = Notas / Tamanho;

        admin.firestore().collection('notasUsuarios/').doc(loja).set(Dados);

        const DadosLojas = Loja.data();
        let Quantidade = DadosLojas['quantidadeAvaliacoesRating'];
        Quantidade = NotasChaves.length;
        DadosLojas['quantidadeAvaliacoesRating'] = Quantidade;
        DadosLojas['mediaRanking'] = MediaFinal;

        admin.firestore().collection('lojas/').doc(loja).set(DadosLojas);

        return "ok";
    })
    .catch(erro =>{
        console.log(erro);
        return "erro";
    })

})

exports.atualizarLista = functions.https.onCall(async (data, _context) =>{

    const NovoDoc = data["doc"];

    const Documentos = await admin.firestore().collection('notasUsuarios').get();

    Documentos.forEach(elemento =>{
         elemento.ref.get().then(valor =>{
            admin.firestore().collection(NovoDoc).doc(valor.id).set(valor.data());
            const valores = valor.data();
            valores["filaNormal"] = {};
            valores["filaRapida"] = {};
            valores["filaPreferencial"] = {};
            valores["notasRanking"] = {};
            admin.firestore().collection('notasUsuarios').doc(valor.id).set(valores);
            admin.firestore().collection('lojas').doc(valor.id).update("filaNormal", {});
            admin.firestore().collection('lojas').doc(valor.id).update("filaRapida", {});
            admin.firestore().collection('lojas').doc(valor.id).update("filaPreferencial", {});
            admin.firestore().collection('lojas').doc(valor.id).update("quantidadeAvaliacoesFila", 0);
            admin.firestore().collection('lojas').doc(valor.id).update("quantidadeAvaliacoesRating", 0);
            console.log("Coleção " + NovoDoc + " Criado com sucesso");
            return "ok"
        })
        .catch(erro =>{
            console.log(erro);
            return "erro";
        })
    })
    return "ok";
})

exports.fazerMedia = functions.firestore.document('usuarios/{uid}').onWrite((change, _context) => {

    const data = change.after.data();
    const Cadastro = data['pontosCadastro'];
    const Fila = data['pontosFila'];
    const Locais = data['pontosLocais'];

    const Soma = Cadastro + Fila + Locais;

    return change.after.ref.set({ pontosTotais: Soma}, {merge: true});
});

exports.recuperarRaking = functions.https.onCall(async (data, _context) =>{

    const id = data['id'];

    return await admin.firestore().collection('notasUsuarios').doc(id).get().then(snapshot =>{
        const dataS = snapshot.data();
        const notas = dataS['notasRanking'];

        let ans = new Map();
        const chaves = Object.keys(notas);

        for (let index = 0; index < chaves.length && index < 20; index++) {
            let valorChave = chaves[index];
            ans[valorChave] = notas[valorChave];
        }
        return ans;
    })
    .catch(erro =>{
        console.log(erro);
        return null;
    });

})