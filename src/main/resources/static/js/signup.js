// 회원가입 폼 제출
document.getElementById('signupForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const nickname = document.getElementById('nickname').value;

    if (!username.trim()) {
        alert('아이디를 입력해주세요.');
        return;
    }

    if (!password) {
        alert('비밀번호를 입력해주세요.');
        return;
    }

    if (!nickname.trim()) {
        alert('닉네임을 입력해주세요.');
        return;
    }

    try {
        const response = await fetch('/api/members/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: username,
                password: password,
                nickname: nickname
            })
        });

        const result = await response.text();

        if (response.ok) {
            alert('회원가입이 완료되었습니다!');
            location.href = '/login.html';
        } else {
            alert(result);
        }
    } catch (error) {
        console.error('회원가입 실패:', error);
        alert('회원가입 중 오류가 발생했습니다.');
    }
});
