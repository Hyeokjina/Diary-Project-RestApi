// 로그인 폼 제출
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    if (!username.trim()) {
        alert('아이디를 입력해주세요.');
        return;
    }

    if (!password) {
        alert('비밀번호를 입력해주세요.');
        return;
    }

    try {
        const response = await fetch('/api/members/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: username,
                password: password
            })
        });

        const result = await response.json();
        
        if (response.ok) {
            // ApiResponse 형식 처리
            const user = result.data || result;
            // 로그인 정보를 localStorage에 저장
            localStorage.setItem('user', JSON.stringify({
                id: user.id,
                email: user.email,
                nickname: user.nickname
            }));
            alert(`${user.nickname}님, 환영합니다!`);
            location.href = '/home.html';
        } else {
            // ApiResponse 형식 처리
            const errorMsg = result.message || result.error || '로그인에 실패했습니다.';
            alert(errorMsg);
        }
    } catch (error) {
        console.error('로그인 실패:', error);
        alert('로그인 중 오류가 발생했습니다.');
    }
});
