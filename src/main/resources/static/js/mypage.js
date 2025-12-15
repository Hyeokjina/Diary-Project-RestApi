// 로그인 상태 확인
function checkLoginStatus() {
    const user = JSON.parse(localStorage.getItem('user'));

    if (user) {
        // 로그인 상태
        document.getElementById('userNickname').textContent = `${user.nickname}님`;
        document.getElementById('myPageBtn').style.display = 'inline-block';
        document.getElementById('logoutBtn').style.display = 'inline-block';
        document.getElementById('loginBtn').style.display = 'none';
        document.getElementById('signupBtn').style.display = 'none';

        // 회원 정보 로드
        loadUserInfo(user.id);
    } else {
        // 비로그인 상태 - 로그인 페이지로 리다이렉트
        location.href = '/login.html';
    }
}

// 로그아웃
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('user');
    location.href = '/home.html';
});

// 회원 정보 로드
async function loadUserInfo(userId) {
    try {
        const response = await fetch(`/api/members/${userId}`);
        const result = await response.json();
        
        // ApiResponse 형식 처리
        const user = result.data || result;

        if (user) {
            document.getElementById('email').value = user.email || '';
            document.getElementById('nickname').value = user.nickname || '';
            // 비밀번호는 로드하지 않음 (보안)
        }
    } catch (error) {
        console.error('회원 정보 로드 실패:', error);
        alert('회원 정보를 불러오는데 실패했습니다.');
    }
}

// 회원 정보 수정
document.getElementById('updateForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const user = JSON.parse(localStorage.getItem('user'));
    if (!user) {
        alert('로그인이 필요합니다.');
        location.href = '/login.html';
        return;
    }

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const passwordConfirm = document.getElementById('passwordConfirm').value;
    const nickname = document.getElementById('nickname').value.trim();
    const errorMessage = document.getElementById('errorMessage');

    // 유효성 검증
    if (!email) {
        errorMessage.textContent = '이메일을 입력해주세요.';
        errorMessage.style.display = 'block';
        return;
    }

    if (password.length < 6) {
        errorMessage.textContent = '비밀번호는 6자 이상이어야 합니다.';
        errorMessage.style.display = 'block';
        return;
    }

    if (password !== passwordConfirm) {
        errorMessage.textContent = '비밀번호가 일치하지 않습니다.';
        errorMessage.style.display = 'block';
        return;
    }

    if (!nickname) {
        errorMessage.textContent = '닉네임을 입력해주세요.';
        errorMessage.style.display = 'block';
        return;
    }

    try {
        const response = await fetch(`/api/members/${user.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password,
                nickname: nickname
            })
        });

        const result = await response.json();

        if (response.ok) {
            // ApiResponse 형식 처리
            const updatedUser = result.data || result;
            
            // localStorage 업데이트
            localStorage.setItem('user', JSON.stringify({
                id: updatedUser.id,
                email: updatedUser.email,
                nickname: updatedUser.nickname
            }));

            alert('회원 정보가 수정되었습니다!');
            location.href = '/home.html';
        } else {
            // ApiResponse 형식 처리
            const errorMsg = result.message || result.error || '회원 정보 수정에 실패했습니다.';
            errorMessage.textContent = errorMsg;
            errorMessage.style.display = 'block';
        }
    } catch (error) {
        console.error('회원 정보 수정 실패:', error);
        errorMessage.textContent = '회원 정보 수정 중 오류가 발생했습니다.';
        errorMessage.style.display = 'block';
    }
});

// 회원 탈퇴
document.getElementById('deleteBtn').addEventListener('click', async () => {
    if (!confirm('정말 탈퇴하시겠습니까?')) {
        return;
    }

    const user = JSON.parse(localStorage.getItem('user'));
    if (!user) {
        alert('로그인이 필요합니다.');
        location.href = '/login.html';
        return;
    }

    try {
        const response = await fetch(`/api/members/${user.id}`, {
            method: 'DELETE'
        });

        const result = await response.json();

        if (response.ok) {
            localStorage.removeItem('user');
            alert('회원 탈퇴가 완료되었습니다.');
            location.href = '/home.html';
        } else {
            // ApiResponse 형식 처리
            const errorMsg = result.message || result.error || '회원 탈퇴에 실패했습니다.';
            alert(errorMsg);
        }
    } catch (error) {
        console.error('회원 탈퇴 실패:', error);
        alert('회원 탈퇴 중 오류가 발생했습니다.');
    }
});

// 페이지 로드 시 실행
checkLoginStatus();

