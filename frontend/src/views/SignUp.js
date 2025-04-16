import React, { useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import InputField from "../components/InputField";
import FormWrapper from "../components/FormWrapper";
import ActionButton from "../components/ActionButton";

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
            if (error.response) {
                alert(error.response.data.error);
            }
            else  {
                alert("회원가입 요청 실패")
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
            <FormWrapper>
                <InputField
                    label="이메일"
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    id="signupEmail"
                />
                <InputField
                    label="비밀번호"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    id="signupPassword"
                />
                <InputField
                    label="이름"
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    id="signupUsername"
                />
                <InputField
                    label="닉네임"
                    type="text"
                    value={nickname}
                    onChange={(e) => setNickname(e.target.value)}
                    id="signupNickname"
                />
                <ActionButton type={"button"} className={'btn btn-primary'} onClick={signUp}>가입</ActionButton>
            </FormWrapper>
            <Link to="/">메인으로 이동</Link> / <Link to="/sign-in">로그인</Link>
        </main>
    );
}

export default SignUp;