const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

/*exports.mediaRating = functions.firestore.document('usuarios/{uid}').onWrite((change, context) => {

    const data = change.after.data();
    const previousData = change.before.data();

    return change.after.ref.set({ pontosCadastro: 50 + previousData['pontosCadastro']}, {merge: true});
})*/


exports.adicionarFilaNormal = functions.firestore.document('notasUsuarios/{uid}').onWrite((change, context) => {

    const data = change.after.data()['filaNormal'];
    const previousData = change.before.data()['filaNormal'];
    const previousTeste = change.before.data()['teste']

    const asd = previousTeste.concat(data[11]);

    return change.after.ref.set({ teste: asd }, {merge: true});

})