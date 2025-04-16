import axios from "axios";
import React, {useRef} from "react";
import { Link, useNavigate } from "react-router-dom"; 
import { useUser } from "../context/UserContext";
import {getAccessToken} from "../utils/jwt";
import InputField from "../components/InputField";
import FormWrapper from "../components/FormWrapper";
import TextareaField from "../components/TextareaField";
import ActionButton from "../components/ActionButton";

function WriteBoard() {
    const { state } = useUser();
    const title = useRef(); //한 번만 가져와서 서버로 보내고 이동하는 기능이기에 useState로 상태 처리 없이 사용
    const content = useRef();
    const memberId = state?.memberId
    const navigate = useNavigate();
    const token = getAccessToken();

    const writeBoard = async (e) => {
        e.preventDefault();

        if(!memberId) {
            alert("유저 정보를 받아올 수 없습니다.");
            return;
        }

        if (!title || !content) {
            alert("모든 필드를 입력하세요.");
            return;
        }

        try {
            const response = await axios.post("/api/write-board", {
                memberId,
                title: title.current.value,
                content: content.current.value

            },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
            alert("등록 완료");
            navigate("/");
        } catch (error) {
            console.error("게시글 등록오류", error);
            if (error.response && error.response.data.error) {
                alert(error.response.data.error); 
            } else {
                alert("게시글 등록 실패 다시 시도해주세요.");
            }
        }

    }

    return (
        <main className="main">
            <FormWrapper>
                <InputField
                    label="제목"
                    type="text"
                    id="title"
                    ref={title}
                />
                <TextareaField
                    label="내용"
                    type="text"
                    id="content"
                    ref={content}
                />
                <ActionButton type={"button"} className={"btn btn-primary"} onClick={writeBoard}>글쓰기</ActionButton>
            </FormWrapper>
            <Link to="/">메인으로 돌아가기</Link>
        </main>
    );
}
export default WriteBoard;