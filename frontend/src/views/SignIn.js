import axios from "axios";
import React, { useState } from "react";
import { useUser } from "../context/UserContext";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import InputField from "../components/InputField";
import FormWrapper from "../components/FormWrapper";
import ActionButton from "../components/ActionButton";
import { setTokens, getUserFromToken } from "../utils/jwt.js";



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
            const response = await axios.post("/api/sign-in", { email, password }, { withCredentials: true });
            console.log("✅ 로그인 요청 성공");

            // 🔹 유저 정보 가져와서 상태 업데이트
            const userResponse = await axios.get("/api/user", { withCredentials: true });

            const user = userResponse.data;
            if(user) {
                setState(user);
                console.log("저장된 유저 정보", user);
                alert("로그인 성공");
                navigate("/");
            } else {
                alert("성공하였으나 사용자 정보를 가져오지 못함")
            }

        } catch (error) {
            console.error("로그인 에러: ", error);

            if (error.response) {
                alert(error.response.data.error);
                }
            else {
                alert("로그인 요청 에러")
            }
        }
    };

    return (
    <main className="main">
        <FormWrapper>
            <InputField
                label="이메일"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                id="signinEmail"
            />
            <InputField
                label="비밀번호"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                id="signinEmail"
            />
            <ActionButton type={"button"} className={'btn btn-primary'} onClick={signIn}>로그인</ActionButton>
        </FormWrapper>
        <Link to="/">메인으로 이동</Link> / <Link to="/sign-up">회원가입</Link>
    </main>
    );
}

export default SignIn;
