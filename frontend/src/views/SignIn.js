import axios from "axios";
import React, { useState } from "react";
import { useUser } from "../context/UserContext";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";



function SignIn() {
    const { setState } = useUser();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();



    const signIn = async (e) => {
        e.preventDefault();
        if(!email || !password) {
            alert('아이디와 비밀번호를 입력해 주세요')
            return;
        }
        try {
            // 로그인 요청
            await axios.post("/api/sign-in", {
                email,
                password
            }, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            console.log("✅ 로그인 요청 성공:");

            try {
            // 로그인 후 세션에서 사용자 정보 가져오기
                const response = await axios.get("/api/user", {
                    withCredentials: true
                });

                if (response.data) {
                    console.log("✅ 세션에서 사용자 정보 가져옴:", response.data);
                    setState(response.data); // UserContext 상태 업데이트
                    sessionStorage.setItem("user", JSON.stringify(response.data)); // 세션 유지

                    alert("로그인 성공!");
                    navigate("/");
                }
            } catch (error) {
                alert("로그인 성공했지만 가져올 수있는 사용자 정보가 없음");
            }
        } catch (error) {
            console.error("로그인 에러: ", error);
            alert("로그인 실패! 다시 시도해주세요.");
        }
    };


    return (
    <main className="main">
        <form id="signInForm" onSubmit={signIn}>
            <div className="mb-3">
                <label htmlFor="exampleInputEmail1" className="form-label">Email address</label>
                <input type="email" className="form-control" id="exampleInputEmail1" value={email} onChange={(e) => setEmail(e.target.value)} />
            </div>
            <div className="mb-3">
                <label htmlFor="exampleInputPassword1" className="form-label">Password</label>
                <input type="password" className="form-control" id="exampleInputPassword1" value={password} onChange={(e) => setPassword(e.target.value)} aria-describedby="passwordHelp" />
                <div id="passwordHelp" className="form-text">
                알파벳, 숫자, 특수 기호를 쓰십시오
                {/* 검증 미구현 */}
                </div>
            </div>
            <button type="submit" className="btn btn-primary">로그인</button>
        </form>
        <Link to="/">메인으로 이동</Link> / <Link to="/sign-up">회원가입</Link>
    </main>
    );
}

export default SignIn;
