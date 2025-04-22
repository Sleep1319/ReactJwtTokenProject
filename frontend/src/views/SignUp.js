import React, { useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import InputField from "../components/InputField";
import FormWrapper from "../components/FormWrapper";
import ActionButton from "../components/ActionButton";
import {Box, Button, TextField, Typography} from "@mui/material";

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
    //온서브밋 = 버튼 타입 서브밋 온서브밋x = 버튼에 온클릭
    return (
        <main className="main" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', paddingTop: '2rem' }}>
            <Typography variant="h5" gutterBottom>회원가입</Typography>

            <Box component="form" onSubmit={signUp} noValidate sx={{ width: '100%', maxWidth: 400, display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                    label="이메일"
                    type="email"
                    fullWidth
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <TextField
                    label="비밀번호"
                    type="password"
                    fullWidth
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <TextField
                    label="이름"
                    type="text"
                    fullWidth
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <TextField
                    label="닉네임"
                    type="text"
                    fullWidth
                    value={nickname}
                    onChange={(e) => setNickname(e.target.value)}
                />
                <Button variant="contained" type="submit" color="primary" fullWidth>
                    회원가입
                </Button>
            </Box>

            <Box sx={{ marginTop: 2 }}>
                <Link to="/">메인으로 이동</Link> / <Link to="/sign-up">회원가입</Link>
            </Box>
        </main>
    );
}

export default SignUp;