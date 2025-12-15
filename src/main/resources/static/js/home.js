// 감정 데이터
const EMOTIONS = [
    { value: 'happy', emoji: '😊', label: '좋았어', color: '#4ECDC4' },
    { value: 'sad', emoji: '😢', label: '힘들다..', color: '#5C7AEA' },
    { value: 'normal', emoji: '😐', label: '그냥 그래', color: '#45B649' },
    { value: 'fire', emoji: '🔥', label: '최고!', color: '#FF6B6B' }
];

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
        document.getElementById('loginPrompt').style.display = 'none';
        document.getElementById('homeContent').style.display = 'block';

        // 감정 통계 로드
        loadEmotionStats(user.id);
    } else {
        // 비로그인 상태
        document.getElementById('userNickname').textContent = '';
        document.getElementById('myPageBtn').style.display = 'none';
        document.getElementById('logoutBtn').style.display = 'none';
        document.getElementById('loginBtn').style.display = 'inline-block';
        document.getElementById('signupBtn').style.display = 'inline-block';
        document.getElementById('loginPrompt').style.display = 'block';
        document.getElementById('homeContent').style.display = 'none';
    }
}

// 로그아웃
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('user');
    location.href = '/home.html';
});

// 감정별 통계 로드
async function loadEmotionStats(memberId) {
    try {
        const statsGrid = document.getElementById('statsGrid');
        statsGrid.innerHTML = '';

        for (const emotion of EMOTIONS) {
            const response = await fetch(`/api/members/${memberId}/diaries?emotion=${emotion.value}`);
            const result = await response.json();
            
            // ApiResponse 형식 처리
            const diaries = result.data || result;
            const count = Array.isArray(diaries) ? diaries.length : 0;

            const statCard = document.createElement('div');
            statCard.className = `stat-card ${emotion.value}`;
            statCard.innerHTML = `
                <div class="stat-emoji">${emotion.emoji}</div>
                <div class="stat-count">${count}</div>
                <div class="stat-label">${emotion.label}</div>
            `;
            statsGrid.appendChild(statCard);
        }
    } catch (error) {
        console.error('감정 통계 로드 실패:', error);
    }
}

// 페이지 로드 시 실행
checkLoginStatus();

