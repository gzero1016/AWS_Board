import axios from "axios";

// create : 전역설정(공통설정)
export const instance = axios.create({
    baseURL: "http://localhost:8080"
});