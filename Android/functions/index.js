const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();


exports.mudarValor = functions.firestore.document('usuarios/{usuarioId}').onWrite((change, context) => {

    const nome = change.before.data().titulo;
    return change.ref.update({
        pontosLocais: 452
    });

});