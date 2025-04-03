import React from "react";
import {createContext, useContext, useState, useEffect} from "react";
import axios from "axios";
//세션관리 및 스테이트 이동
const UserContext = createContext();

//자식들에게 상태갑 프롭 (모든 사이트에 유저 정보를 바로 연결시키기 위함)
export function UserProvider({children}) {
    const [state, setState] = useState(null);

    useEffect(() => {
        const validateSession = async () => {
            try {
                console.log("세션 유효 확인 시작")
                // 서버에서 유효한 세션인지 확인
                const response = await axios.get("/api/user/", {
                    withCredentials: true  // 세션 쿠키 포함
                });
                console.log("요청 진행 후")
                if (response.data) {
                    console.log("응답 완료 데이터: ",response.data);
                    setState(response.data);  // 서버에서 받은 데이터로 상태 설정
                }
            } catch (error) {
                console.log("세션이 만료되었거나 유효하지 않습니다.");
                // setState(null);  // 세션이 유효하지 않으면 상태 초기화
                // window.location.href = "/sign-in";
            }
        };

        validateSession();
    }, []);

    const logout = async () => {
        try {
            // 서버에 로그아웃 요청
            await axios.post("/api/logout", {}, {
                withCredentials: true  // 세션 쿠키 포함
            });
        } catch (error) {
            console.error("로그아웃 요청 실패:", error);
        } finally {
            setState(null);  // 상태 초기화
            window.location.href = "/";  // 로그아웃 후 리다이렉션
        }
    };

    return (<UserContext.Provider value={{state, setState, logout}}>
            {children}
        </UserContext.Provider>);
}

export function useUser() {
    return useContext(UserContext);
}