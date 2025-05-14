import React, {useEffect, useState} from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { Box, Button, TextField, Typography } from "@mui/material";

function SocialSignUp() {
    const location = useLocation();
    const navigate = useNavigate();
    const query = new URLSearchParams(location.search);

    const email = query.get("email");
    const name = query.get("name");
    const provider = query.get("provider");
    const providerId = query.get("providerId");
    const reason = query.get("reason");

    const [nickname, setNickname] = useState("");

    useEffect(() => {
        if (reason === "not-registered") {
            alert("등록되지 않은 회원입니다. 닉네임을 입력하여 회원가입을 완료해주세요.");
        }
    }, [reason]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!nickname) {
            alert("닉네임을 입력하세요.");
            return;
        }

        try {
            await axios.post("/api/social-sign-up", {
                email,
                username: name, // 구글 이름 그대로 사용
                nickname,
                provider,
                providerId
            });

            // 회원가입 성공 시 다시 로그인 이동
            window.location.href = "http://localhost:8084/oauth2/authorization/google"

        } catch (error) {
            console.error("소셜 회원가입 실패", error);
            if (error.response?.data) {
                alert(error.response.data.message || "회원가입 실패");
            } else {
                alert("회원가입 요청 실패");
            }
        }
    };

    return (
        <main className="main" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', paddingTop: '2rem' }}>
            <Typography variant="h5" gutterBottom>소셜 회원가입</Typography>

            <Box component="form" onSubmit={handleSubmit} sx={{ width: '100%', maxWidth: 400, display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                    label="이메일"
                    value={email}
                    disabled
                    fullWidth
                />
                <TextField
                    label="이름"
                    value={name}
                    disabled
                    fullWidth
                />
                <TextField
                    label="닉네임"
                    value={nickname}
                    onChange={(e) => setNickname(e.target.value)}
                    fullWidth
                />
                <Button variant="contained" type="submit" color="primary" fullWidth>
                    회원가입 완료
                </Button>
            </Box>
        </main>
    );
}

export default SocialSignUp;