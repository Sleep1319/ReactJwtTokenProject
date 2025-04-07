import React from "react";
import {createContext, useContext, useState, useEffect} from "react";
import {clearTokens, getUserFromToken} from "../utils/jwt";
import {useNavigate} from "react-router-dom";
//세션관리 및 스테이트 이동
const UserContext = createContext();

//자식들에게 상태갑 프롭 (모든 사이트에 유저 정보를 바로 연결시키기 위함)
export function UserProvider({ children }) {
    const [state, setState] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const user = getUserFromToken(); //
        if (user) {
            setState(user);
        } else {
            setState(null);//첫 접속시 토큰이 없기에 로그인 화연으로 이동
            // logout(); // 토큰이 없거나 만료되었으면 로그아웃
        }
    }, []);

    const logout = () => {
        clearTokens(); //
        setState(null);
        navigate("/sign-in");
    };


    return (<UserContext.Provider value={{state, setState, logout}}>
            {children}
        </UserContext.Provider>);
}

export function useUser() {
    return useContext(UserContext);
}