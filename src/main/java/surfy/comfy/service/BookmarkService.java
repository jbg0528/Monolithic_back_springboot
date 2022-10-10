package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.bookmark.PostBookmarkResponse;
import surfy.comfy.data.post.PostResponse;
import surfy.comfy.entity.Bookmark;
import surfy.comfy.entity.Member;
import surfy.comfy.repository.BookmarkRepository;
import surfy.comfy.repository.MemberRepository;
import surfy.comfy.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    /**
     * 북마크한 글 조회
     * @param memberId
     * @return
     */
    @Transactional
    public List<PostResponse> getBookmarks(Long memberId){

        // 내가 북마크한 게시글들
        List<Bookmark> bookmarkList=bookmarkRepository.findAllByMember_Id(memberId);
        List<PostResponse> bookmarks=bookmarkList.stream()
                .map(b->new PostResponse(b))
                .collect(Collectors.toList());

        return bookmarks;
    }

    /**
     * 북마크 추가
     * @param postId
     * @param memberId
     * @return
     */
    @Transactional
    public String addBookmark(Long postId, Long memberId){
        Bookmark bookmark=new Bookmark();
        bookmark.setMember(memberRepository.findById(memberId).get());
        bookmark.setPost(postRepository.findById(postId).get());

        bookmarkRepository.save(bookmark);

        return "북마크 추가 성공";
    }

    @Transactional
    public String deleteBookmark(Long postId,Long memberId){
        Bookmark bookmark= bookmarkRepository.findByMember_IdAndPost_Id(memberId,postId);
        bookmarkRepository.delete(bookmark);

        return "북마크 삭제 성공";
    }
}
