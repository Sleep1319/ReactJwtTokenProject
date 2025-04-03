import {useEffect, useState} from "react";
import axios from "axios";

const [state, setState] = useState(() => {
    const savedUser = sessionStorage.getItem("user");
    return savedUser ? JSON.parse(savedUser) : null;
});

//서버 세션 조회
useEffect(() => {
    const validateSession = async () => {
        try {
            // 세션 쿠키가 유효한지 확인
            const response = await axios.get("/api/user/", {
                withCredentials: true
            });

            if (response.data) {
                setState(response.data);
                sessionStorage.setItem("user", JSON.stringify(response.data));
            }
        } catch (error) {
            console.log("세션이 만료되었거나 유효하지 않습니다.");
            sessionStorage.removeItem("user");
            setState(null);
        }
    };

    // 세션 스토리지에 사용자 정보가 있으면 검증
    if (sessionStorage.getItem("user")) {
        validateSession();
    }
}, []);