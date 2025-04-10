import React from "react";
import {createContext, useContext, useState, useEffect} from "react";
import {clearTokens, getUserFromToken} from "../utils/jwt";
import {useNavigate} from "react-router-dom";
import axios from "axios";
//세션관리 및 스테이트 이동
const UserContext = createContext();

//자식들에게 상태갑 프롭 (모든 사이트에 유저 정보를 바로 연결시키기 위함)
export function UserProvider({ children }) {
    const [state, setState] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axios.get("/api/user", { withCredentials: true });
                setState(response.data);
            } catch (error) {
                console.log("사용자 정보 없거나 만료", error);
                if (!state == null) {
                    setState(null);
                    navigate("")
                }
            }
        }
            fetchUser();
    }, []);

    const logout = async () => {
        try {
            await axios.post("/api/logout", {}, { withCredentials: true });
        } catch (error) {
            console.error("로그아웃 요청 실패", error)
        }

        setState(null);
        navigate("/sign-in");
    }


    return (<UserContext.Provider value={{state, setState, logout}}>
            {children}
        </UserContext.Provider>);
}

export function useUser() {
    return useContext(UserContext);
}