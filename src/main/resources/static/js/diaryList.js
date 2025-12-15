// 감정 이모지 매핑
const EMOTIONS = {
    'happy': '😊',
    'sad': '😢',
    'normal': '😐',
    'fire': '🔥'
};

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

        // 일기 목록 로드
        loadDiaries(user.id);
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

// 검색 기능
let isSearching = false;
let searchKeyword = '';

document.getElementById('searchInput').addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
        const keyword = document.getElementById('searchInput').value.trim();
        const user = JSON.parse(localStorage.getItem('user'));
        
        if (keyword) {
            isSearching = true;
            searchKeyword = keyword;
            if (user) {
                loadDiaries(user.id, null, keyword);
            }
        } else {
            isSearching = false;
            searchKeyword = '';
            if (user) {
                loadDiaries(user.id);
            }
        }
    }
});

// 일기 목록 로드
async function loadDiaries(memberId, emotion = null, keyword = null) {
    try {
        let url = `/api/members/${memberId}/diaries`;
        if (emotion) {
            url += `?emotion=${emotion}`;
        } else if (keyword) {
            url += `?keyword=${keyword}`;
        }

        const response = await fetch(url);
        const result = await response.json();
        
        // ApiResponse 형식 처리
        const diaries = result.data || result;

        const diaryList = document.getElementById('diaryList');
        const emptyState = document.getElementById('emptyState');

        diaryList.innerHTML = '';

        if (!Array.isArray(diaries) || diaries.length === 0) {
            emptyState.style.display = 'block';
            return;
        }

        emptyState.style.display = 'none';

        // 최신순 정렬
        const sortedDiaries = [...diaries].sort((a, b) => {
            const dateA = new Date(a.createdAt || a.date);
            const dateB = new Date(b.createdAt || b.date);
            return dateB - dateA;
        });

        sortedDiaries.forEach(diary => {
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

