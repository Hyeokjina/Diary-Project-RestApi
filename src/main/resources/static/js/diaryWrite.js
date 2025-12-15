// 로그인 체크
const user = JSON.parse(localStorage.getItem('user'));
if (!user) {
    alert('로그인이 필요합니다.');
    location.href = '/login.html';
}

// 오늘 날짜 표시
const today = new Date().toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
});
document.getElementById('dateDisplay').textContent = today;

// 감정 선택
let selectedEmotion = 'fire'; // 기본값

const emotionButtons = document.querySelectorAll('.emotion-btn');
emotionButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        // 모든 버튼에서 active 제거
        emotionButtons.forEach(b => b.classList.remove('active'));
        // 클릭한 버튼에 active 추가
        btn.classList.add('active');
        selectedEmotion = btn.dataset.emotion;
    });
});

// 일기 작성 폼 제출
document.getElementById('diaryWriteForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const content = document.getElementById('content').value;
    const errorMessage = document.getElementById('errorMessage');

    if (content.trim().length === 0) {
        errorMessage.textContent = '일기 내용을 입력해주세요.';
        errorMessage.style.display = 'block';
        return;
    }

    if (content.trim().length < 5) {
        errorMessage.textContent = '최소 5자 이상 입력해주세요.';
        errorMessage.style.display = 'block';
        return;
    }

    try {
        const response = await fetch('/api/diaries', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                memberId: user.id,
                title: content.substring(0, 20), // 내용 앞 20자를 제목으로
                content: content.trim(),
                emotion: selectedEmotion
            })
        });

        const result = await response.json();

        if (response.ok) {
            alert('일기가 작성되었습니다!');
            location.href = '/diaryList.html';
        } else {
            // ApiResponse 형식 처리
            const errorMsg = result.message || result.error || '일기 작성에 실패했습니다.';
            errorMessage.textContent = errorMsg;
            errorMessage.style.display = 'block';
        }
    } catch (error) {
        console.error('일기 작성 실패:', error);
        errorMessage.textContent = '일기 작성 중 오류가 발생했습니다.';
        errorMessage.style.display = 'block';
    }
});
