import React, { useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

function SignUp() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [username, setUsername] = useState("");
    const [nickname, setNickname] = useState("");

    const signUp = async (e) => {
        e.preventDefault(); // 폼 기본 제출 동작 막기

        if (!email || !password || !username || !nickname) {
            alert("모든 필드를 입력하세요.");
            return;
        }

        try {
            await axios.post("/api/sign-up", {
                email,
                password,
                username,
                nickname
            });

            alert("회원가입 성공");
            window.location.href="/sign-in";
        } catch (error) {
            console.error("회원가입 오류: ", error);
            if (error.response && error.response.status === 400) {

                const errors = error.response.data;
                let errorMessage = "";

                for (const field in errors) {
                    errorMessage += `${errors[field]}\n`;
                }
                console.log("에러메시지", errorMessage)
                alert(errorMessage);
            }
            resetForm();
        }
    };




    const resetForm = () => {
        setEmail("");
        setPassword("");
        setUsername("");
        setNickname("");
    };

    return (
        <main className="main">
            <form id="signUpForm" onSubmit={signUp}>
                <div className="mb-3">
                    <label htmlFor="exampleInputEmail1" className="form-label">이메일</label>
                    <input type="email" className="form-control" id="exampleInputEmail1" value={email} onChange={(e) => setEmail(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label htmlFor="exampleInputPassword1" className="form-label">비밀번호</label>
                    <input type="password" className="form-control" id="exampleInputPassword1" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label htmlFor="exampleInputUsername" className="form-label">이름</label>
                    <input type="text" className="form-control" id="exampleInputUsername" value={username} onChange={(e) => setUsername(e.target.value)} />
                </div>
                <div className="mb-3">
                    <label htmlFor="exampleInputNickname" className="form-label">닉네임</label>
                    <input type="text" className="form-control" id="exampleInputNickname" value={nickname} onChange={(e) => setNickname(e.target.value)} />
                </div>
                <button type="submit" className="btn btn-primary" >가입</button>
                <button type="reset" className="btn btn-secondary">다시</button>
            </form>
            <Link to="/">메인으로 이동</Link> / <Link to="/sign-in">로그인</Link>
        </main>
    );
}

export default SignUp;