import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://192.168.100.3:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

export default axiosClient;