// const express =require('express');
// const mysql = require('mysql');
// const cors = require('cors');
// const session = require('express-session')
//
// const app = express();
// app.use(cors({
//     credential: true
// }));
// app.use(express.json());
//
// app.use(session({
//     secret: "mySecretKey",
//     resave: false,
//     saveUninitialized: false,
//     cookie: { secure: false } // HTTPS가 아니므로 false
// }));
//
// const db = mysql.createConnection({
//     host: 'localhost',
//     port: '3306',
//     user: 'root',
//     password: 'Sleep1205!',
//     database: 'Apiboard4'
// });
//
// db.connect((err) => {
//     if(err) {
//         console.log("MySql 연결 실패: " + err);
//     } else {
//         console.log("MySql 연결 성공" )
//     }
// });
//
// //회원가입
// app.post("/api/sign-up", (req, res) => {
//     const { email, password, username, nickname } = req.body;
//     const emailCheck = "SELECT email FROM member WHERE email = ?";
//     const nickNameCehck = "SELECT nickname FROM member WHERE nickname = ?";
//     const sql = "INSERT INTO member (email, password, username, nickname) VALUES (?, ?, ?, ?)";
//     const roleSql = "insert into member_roles (member_id, role_id) values (?, 2)"; //등급 고정
//
//     //이메일 중복체크크
//     db.query(emailCheck, [email], (err, emailResult) => {
//         if (err) {
//             return res.status(500).json({ error: "회원가입 중 오류 발생", details: err.sqlMessage });
//         }
//         if (emailResult.length > 0) {
//             return res.status(409).json({ error: "이메일 중복!"})
//         }
//         //닉네임 충복체크
//         db.query(nickNameCehck, [nickname], (err, nickNameResult) => {
//             if (err) {
//                 return res.status(500).json({ error: "회원가입 중 오류 발생", details: err.sqlMessage });
//             }
//             if (nickNameResult.length > 0) {
//                 return res.status(409).json({ error: "닉네임 중복!"})
//             }
//
//             db.query(sql, [email, password, username, nickname], (err, result) => {
//                 if (err) {
//                     return res.status(500).json({ error: "회원가입 중 오류 발생", details: err.sqlMessage });
//                 }
//                 const memberId = result.insertId;//저장한 아이디값을 mysql에 뽑아주는 이름
//
//                 db.query(roleSql, [memberId], (err) => {
//                     if (err) return res.status(500).json({ error: "역할 추가 실패 ", details: err.sqlMessage});
//                     res.json({ message: "회원가입 성공!" });
//                 });
//             });
//         });
//     });
// });
//
// //로그인
// app.post("/api/sign-in", (req, res) => {
//     const { email, password } = req.body;
//     const sql =
//     "SELECT m.id, m.email, m.username, m.nickname, r.role_name FROM member m JOIN member_roles mr ON m.id = mr.member_id JOIN roles r ON mr.role_id = r.id WHERE m.email = ? AND m.password = ?"
//
//     db.query(sql, [email, password], (err, results) => {
//         if (err) {
//             console.error("로그인 오류", err);
//             return res.status(500).json({ error: "서버 오류 발생" });
//         }
//
//         if (results.length > 0) {//배열 상태로 오기에 명시시
//             const userData = {
//                 userId: results[0].id,
//                 email: results[0].email,
//                 nickname: results[0].nickname,
//                 roleName: results[0].role_name
//             };
//
//             res.status(200).json({ message: '로그인 성공!', user: userData });
//         } else {
//             res.status(401).json({ error: "이메일 또는 비밀번호가 틀렸습니다." });
//         }
//     });
// });
//
// //게시글 전체 조회회
// app.get("/api/posts", (req, res) => {
//     const sql = "SELECT b.id, b.title, m.nickname FROM board b JOIN member m ON b.member_id = m.id";
//
//     db.query(sql, (err, results) => {
//         if (err) {
//             console.error("DB에러:", err);
//             res.status(500).json({ error: "DB에러" });
//         } else {
//             res.json(results);
//         }
//     });
// });
//
// //id로 게시글 가져오기
// app.get("/api/post/:id", (req, res) => {
//     const { id } = req.params;
//     const sql = "SELECT b.id, b.title, b.content, b.member_id, m.nickname FROM board b JOIN member m ON b.member_id = m.id";
//
//     db.query(sql, (err, result) => {
//         if (err) {
//             console.error("DB에러:", err);
//             res.status(500).json({ error: "DB에러" });
//         } else {
//             res.json(result[0]);
//         }
//     });
// });
//
// //게시글 등록
// app.post("/api/write-board", (req, res) =>{
//     const { title, content, memberId} = req.body;
//     const sql = "INSERT INTO board(title, content, member_id) VALUES(?, ?, ?)";
//
//     db.query(sql, [title, content, memberId], (err) => {
//         if (err) {
//             console.error("DB등록에러: ",err);
//             res.status(500).json({ error: "DB에러"});
//         } else {
//             res.json({message: "게시글 등록 성공"});
//         }
//     });
// });
//
// //게시글 수정
// app.put("/api/update-board/:id", (req, res) => {
//     const { id } = req.params;
//     const { content } = req.body
//     const sql = "UPDATE board SET content = ? WHERE id = ?";
//
//     db.query(sql, [content, id], (err) => {
//         if (err) {
//             console.error("DB수정에러: ", err);
//             res.status(500).json({ error: "DB에러"});
//         } else {
//             res.json({message: "수정 완료"})
//         }
//     });
// });
//
// //게시글 삭제
// app.delete("/api/delete-board/:id", (req, res) => {
//     const { id } = req.params;
//     const sql = "DELETE FROM board WHERE id = ?";
//
//     db.query(sql, [id], (err) => {
//         if(err) {
//             console.error("DB삭제에러: ", err);
//             res.status(500).json({ error: "DB에러" });
//         } else {
//             res.json({ message: "삭제 완료" });
//         }
//     });
// });
//
// // 서버 실행
// app.listen(5000, () => {
//     console.log("서버 실행 중... 포트 5000");
// });