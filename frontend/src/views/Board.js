import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router-dom";
import { Box, Button, TextField } from "@mui/material";
import InputField from "../components/InputField";
import FormWrapper from "../components/FormWrapper";
import TextareaField from "../components/TextareaField";
import ActionButton from "../components/ActionButton";

//"" 값 설정이유 언디파인 대용 콘솔창 에러로그들..
function Board() {
    const { state } = useUser();
    const { id } = useParams();
    const [post, setPost] = useState({ title: "", nickname: "", content: "" });
    const [isReadOnly, setIsReadOnly] = useState(true);
    const [editedContent, setEditedContent] = useState(""); 
    const navigate = useNavigate();

    useEffect(() => {
        const getBoardById = async () => {
            try {
                const response = await axios.get(`/api/board/${id}`);
                setPost(response.data || { title: "", nickname: "", content: "" });
                console.log("서버 응답:", response.data);
            } catch (error) {
                console.error("불러오기 실패", error);
                if (error.response) {
                    alert(error.response.data.error);
                }
                else {
                    alert("게시글 조회 에러")
                }
            }
        };
    
        if (id) getBoardById();
    }, [id]);

    const updateBoard = async () => {
        if (!window.confirm("정말로 수정하시겠습니까??")) return;

        try {
            const response = await axios.put(`/api/board/${id}`, {
                title: post.title,
                content: editedContent
            });
            setPost((prevPost) => ({ ...prevPost, content: editedContent }));
            setIsReadOnly(true);
            alert("수정 완료");
        } catch (error) {
            if (error.response) {
                alert(error.response.data.error);
            } else {
                console.error("수정 실패", error.response)
                alert("수정 실패");
            }
        }
    };

    const deleteBoard = async () => {
        if (!window.confirm("글을 삭제하시겠습니까?")) return;
        
        try {
            const response = await axios.delete(`/api/board/${id}`);
            alert("삭제 완료")
            navigate("/");
        } catch (error) {
            if (error.response) {
                alert(error.response.data.error);
            } else {
                console.error("삭제 실패", error);
                alert("삭제실패");
            }
        }
    }

    useEffect(() => {
        if (post.content) {
            setEditedContent(post.content);
        }
    }, [post]);
    


    const handleCancel = () => {
        if (!window.confirm("수정을 취소하겠습니까?")) return;
        
        setIsReadOnly(true);
        setEditedContent(post.content);
    };
    
    return (
        <main className="main">
            <FormWrapper>
                <div className="d-flex gap-3">
                    <InputField
                        label="제목"
                        type="text"
                        id="title"
                        value={post.title}
                        onChange={() => {}}
                    />
                    <InputField
                        label="작성자"
                        type="text"
                        id="nickname"
                        value={post.nickname}
                        onChange={() => {}}
                    />
                </div>
                <TextareaField
                    label="내용"
                    id="content"
                    rows={20}
                    value={editedContent}
                    onChange={(e) => setEditedContent(e.target.value)}
                    readOnly={isReadOnly}
                />
            </FormWrapper>
            {post && post.memberId && Number(state?.memberId) === Number(post.memberId) && (
                <>
                    {isReadOnly ? (
                            <ActionButton type={"button"} className={"btn btn-warning"} onClick={() => setIsReadOnly(false)}>수정하기</ActionButton>
                    ) : (
                        <>
                            <ActionButton type={"button"} className={"btn btn-success"} onClick={updateBoard}>확인</ActionButton>
                            <ActionButton type={"button"} className={"btn btn-warning"} onClick={handleCancel}>취소</ActionButton>
                        </>
                    )}
                </>
            )}
            {post && post.memberId && state && (Number(state.memberId) === Number(post.memberId) || state.roleName === "ADMIN") && (
                    <button className="btn btn-danger" onClick={deleteBoard}>글 삭제</button>
                )}
            {/*<button className="btn btn-danger" onClick={deleteBoard}>권한 없는 삭제 테스트</button>*/}
            <Link to="/">메인으로 돌아가기</Link>
        </main>
    );
}
export default Board;