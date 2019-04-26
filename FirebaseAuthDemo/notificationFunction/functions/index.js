'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/notifications/{AdminUID}/{notification_id}').onWrite((change, context) => {

    const AdminUID = context.params.AdminUID;
    const notification_id = context.params.notification_id;
    console.log('The admin UID : ', AdminUID);
    
    if(!change.after.val()){

        return console.log('A notification has been deleted from the database : ', notification_id);
    }

    const notificationData = admin.database().ref(`/notifications/${AdminUID}/${notification_id}`).once('value');
    return notificationData.then(notificationDataResult => {
        
        const workerName = notificationDataResult.val().workerName;
        const bathroomID = notificationDataResult.val().bathroomID;

        console.log(workerName, ' did not clean bathroom ',bathroomID, ' well' );

        const deviceToken = admin.database().ref(`/Admin/${AdminUID}/device_token`).once('value');

    return deviceToken.then(result => {

        const token_id = "dheg8UZ1Zng:APA91bFyIVn0235NB73NP_B-YTfC3m8J35aXr2ZNorX6AJ1CyBKnFFl1hCL211MeRrXg1MVXNwsyLpNzfKoIG2mMtMTl8eSHGYZJ-mR2ZQe0-PKBXrTN3xaU-eMs8yjeDuXwP3UuUghs";
        const payload = {   
            notification: {
                title : "Maintenance issue",
                body: `${workerName} did not clean bathroom ${bathroomID} well`,
                icon: "default"
            }
        };
    
        return admin.messaging().sendToDevice(token_id, payload).then(response => {
            return console.log('This was the notification feature');
            });
        });
    });

     
    

});

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });