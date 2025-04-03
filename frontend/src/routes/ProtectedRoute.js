import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useUser } from "../context/UserContext";

function ProtectedRoute({ requireAuth }) {
    const { state } = useUser();
    const logIn = state && state.userId;
    
    if (!requireAuth && logIn) {
        return <Navigate to="/" replace />;//상태값에 따라 강제이동 로그인 상태면 강제이동
    }
    if (requireAuth && !logIn) {
        alert('로그인 후 이용해주세요')
        return <Navigate to="/sign-in" replace />;//로그아웃 상태이면 강제이동
    }
    return <Outlet />;
}

export default ProtectedRoute;