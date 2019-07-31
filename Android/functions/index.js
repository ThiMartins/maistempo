const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

/*exports.mediaRating = functions.firestore.document('usuarios/{uid}').onWrite((change, context) => {

    const data = change.after.data();
    const previousData = change.before.data();

    return change.after.ref.set({ pontosCadastro: 50 + previousData['pontosCadastro']}, {merge: true});
})*/


/*exports.adicionarFilaNormal = functions.firestore.document('notasUsuarios/{uid}').onWrite((change, context) => {

    const data = change.after.data()['filaNormal'];
    const previousData = change.before.data()['filaNormal'];
    const previousTeste = change.before.data()['teste']

    const asd = previousTeste.concat(data[11]);

    return change.after.ref.set({ teste: asd }, {merge: true});

})*/


exports.adicionarFila = functions.https.onCall((data, context) => {

    const Documento = data['id'];
    const Valor = data['valor'];

    //const Banco = await admin.firestore().document('notasUsuarios/'`${Documento}`);

    //response.status(200).send("Adicionado");
    const valores = admin.firestore().doc('notasUsuarios/7KcJkXNU8Pn7n4UxYD6B').get()
    .then(snapshot => {
        const Dados = snapshot.data();
        console.log("Foi");
        return Dados;

    })
    .catch(error =>{
        console.log("Erro");
    })
        //valores.then(snapshot =>{
        //    const Data = snapshot.data()

        //})
    //    throw new functions.https.HttpsError('invalid-argument', `${valores}`);
    //return valores
});