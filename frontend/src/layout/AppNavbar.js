import React from "react";
import { Link } from "react-router-dom";
import { useUser } from "../context/UserContext";

function AppNavbar( ) {
    const { state, logout } = useUser();
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
            <div className="container-fluid">
                <Link to="/" className="navbar-brand">UngBoard</Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav">
                        {!state?.email ? (
                            <>
                            <li className="nav-item">
                                <Link to="/sign-in" className="nav-link">로그인</Link>
                            </li>
                            <li className="nav-item">
                                <Link to="/sign-up" className="nav-link">회원가입</Link>
                            </li>
                            </>
                        ) : (
                            <>
                            <li className="nav-item">
                                <span className="nav-link">닉네임: {state.nickname}</span>
                            </li>
                            <li className="nav-item">
                                <span className="nav-link">등급: {state.roleName}</span>
                            </li>
                            <li className="nav-item">
                                <button className="btn btn-danger" onClick={logout}>로그아웃</button>
                            </li>
                            </>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
}

export default AppNavbar;
