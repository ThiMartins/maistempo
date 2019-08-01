const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

/*exports.mediaRating = functions.firestore.document('usuarios/{uid}').onWrite((change, context) => {

    const data = change.after.data();
    const previousData = change.before.data();

    return change.after.ref.set({ pontosCadastro: 50 + previousData['pontosCadastro']}, {merge: true});
})*/


/*exports.fazerMedia = functions.firestore.document('notasUsuarios/{uid}').onWrite((change, context) => {

    const filaNormal = change.after.data()['filaNormal'];
    const filaRapida = change.before.data()['filaNormal'];
    const filaPrefencial = change.before.data()['teste'];

    const asd = previousTeste.concat(data[11]);

    return change.after.ref.set({ teste: asd }, {merge: true});

})*/

exports.notasRanking = functions.https.onCall((data, context) => {

    const DocumentoPassado = data['loja'];
    const ValorPassado = data['valor'];
    const PessoaPassada = data['pessoa'];


    const valores = admin.firestore().doc('notasUsuarios/' + DocumentoPassado).get()
        .then(snapshot => {

            //Recuperar Documento
            const Dados = snapshot.data();
            const MediaAntiga = Dados['mediaRanking'];

            if(MediaAntiga[PessoaPassada].exist()){
                MediaAntiga[PessoaPassada] = ValorPassado;
            } else {
                MediaAntiga.put(PessoaPassada, ValorPassado);
            }

            var Media = 0.0;
            const Tamanho = MediaAntiga.length;
            MediaAntiga.forEach(function(valores) {
                Media += valores;
            });


            const MediaFinal = Media / Tamanho;

            admin.firestore().collection('lojas/').doc(DocumentoPassado).get()
            .then(snapshotDoc =>{
                const DadosMedia = snapshotDoc.data();

                DadosMedia['mediaRanking'] = MediaFinal;

                admin.firestore().collection('lojas/').doc(DocumentoPassado).set(DadosMedia);

                return "ok";

            })
            .catch (error =>{
                console.log(error);
                return "Erro ao mandar a nota";
            });
            admin.firestore().collection('notasUsuarios/').doc(DocumentoPassado).set(Dados);

            return "Fila Atualizado";

        })
        .catch(error =>{
            console.log(error);
            return "Erro ao atualizar lista";
        });
})

exports.adicionarFila = functions.https.onCall((data, context) => {

    const DocumentoPassado = data['id'];
    const ValorPassado = data['valor'];
    const HorarioPassado = data['horario'];
    const TipoFila = data['tipoFila']

    const valores = admin.firestore().doc('notasUsuarios/' + DocumentoPassado).get()
    .then(snapshot => {

        //Recuperar Documento
        const Dados = snapshot.data();
        const FilaNormal = Dados[TipoFila];

        const Horario = FilaNormal[HorarioPassado];
        const ValorR = parseInt(ValorPassado);
        Horario.push(ValorR);
        FilaNormal[HorarioPassado] = Horario;

        var Media = 0.0;
        const Tamanho = Horario.length;
        Horario.forEach(function(valores) {
            Media += valores;
        });


        const MediaFinal = Media / Tamanho;

        admin.firestore().collection('lojas/').doc(DocumentoPassado).get()
        .then(snapshotDoc =>{
            const DadosMedia = snapshotDoc.data();

            const FilaMedia = DadosMedia[TipoFila];
            FilaMedia[HorarioPassado] = MediaFinal;

            admin.firestore().collection('lojas/').doc(DocumentoPassado).set(DadosMedia);

            return "ok";

        })
        .catch (error =>{
            console.log(error);
            return "Erro ao mandar a nota";
        });
        admin.firestore().collection('notasUsuarios/').doc(DocumentoPassado).set(Dados);

        return "Fila Atualizado";

    })
    .catch(error =>{
        console.log(error);
        return "Erro ao atualizar lista";
    });
});