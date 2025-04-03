import axios from 'axios';

// axios 인스턴스를 생성하고, baseURL을 설정
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8084',  // 기본 URL 설정 (Spring Boot 서버 주소)
});

export default axiosInstance;