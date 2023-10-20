// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getStorage } from "firebase/storage";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyBa7EknvXduBQ73iTXBGGsDcUlXVzrrWrM",
    authDomain: "board-af837.firebaseapp.com",
    projectId: "board-af837",
    storageBucket: "board-af837.appspot.com",
    messagingSenderId: "1011282373011",
    appId: "1:1011282373011:web:d1d713a29b33fae9c16066",
    measurementId: "G-JSP32Q4B0J"
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);
export const analytics = getAnalytics(app);
export const storage = getStorage(app); // 핵심