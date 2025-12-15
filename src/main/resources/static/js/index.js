// 감정 이모지 매핑
const EMOTIONS = {
    'happy': '😊',
    'sad': '😢',
    'normal': '😐',
    'fire': '🔥'
};
function setActiveNav() {
    const path = window.location.pathname;

    const homeLink = document.getElementById('homeLink');
    const boardLink = document.getElementById('boardLink');

    if (path === '/' || path === '/index.html') {
        homeLink.classList.add('active');
    } else if (path.includes('board')) {
        boardLink.classList.add('active');
    }
}

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
        document.getElementById('loginMessage').style.display = 'none';
        document.getElementById('diaryContainer').style.display = 'block';

        // 일기 목록 로드 (diaryList.html로 리다이렉트)
        // loadDiaries(user.id);
    } else {
        // 비로그인 상태
        document.getElementById('userNickname').textContent = '';
        document.getElementById('myPageBtn').style.display = 'none';
        document.getElementById('logoutBtn').style.display = 'none';
        document.getElementById('loginBtn').style.display = 'inline-block';
        document.getElementById('signupBtn').style.display = 'inline-block';
        document.getElementById('loginMessage').style.display = 'block';
        document.getElementById('diaryContainer').style.display = 'none';
    }
}

// 로그아웃
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('user');
    location.href = '/home.html';
});

// 일기 목록 로드
async function loadDiaries(memberId) {
    try {
        const response = await fetch(`/api/members/${memberId}/diaries`);
        const result = await response.json();
        
        // ApiResponse 형식 처리
        const diaries = result.data || result;

        const diaryList = document.getElementById('diaryList');
        const emptyState = document.getElementById('emptyState');

        diaryList.innerHTML = '';

        if (diaries.length === 0) {
            emptyState.style.display = 'block';
            return;
        }

        emptyState.style.display = 'none';

        diaries.forEach(diary => {
            const diaryCard = createDiaryCard(diary);
            diaryList.appendChild(diaryCard);
        });
    } catch (error) {
        console.error('일기 목록 로드 실패:', error);
        alert('일기 목록을 불러오는데 실패했습니다.');
    }
}

// 일기 카드 생성
function createDiaryCard(diary) {
    const div = document.createElement('div');
    div.className = 'diary-card';
    div.onclick = () => location.href = `/diaryDetail.html?id=${diary.id}`;

    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    const dateStr = diary.date || diary.createdAt;
    const content = diary.content || diary.title || '';

    div.innerHTML = `
        <div class="diary-card-date">${formatDate(dateStr)}</div>
        <div class="diary-card-emotion">${EMOTIONS[diary.emotion] || '😐'}</div>
        <div class="diary-card-content">${content}</div>
    `;

    return div;
}

// 페이지 로드 시 실행
checkLoginStatus();
setActiveNav();