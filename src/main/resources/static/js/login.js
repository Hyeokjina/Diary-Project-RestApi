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

        if (response.ok) {
            const user = await response.json();
            // 로그인 정보를 localStorage에 저장
            localStorage.setItem('user', JSON.stringify(user));
            alert(`${user.nickname}님, 환영합니다!`);
            location.href = '/';
        } else {
            const error = await response.text();
            alert(error);
        }
    } catch (error) {
        console.error('로그인 실패:', error);
        alert('로그인 중 오류가 발생했습니다.');
    }
});
