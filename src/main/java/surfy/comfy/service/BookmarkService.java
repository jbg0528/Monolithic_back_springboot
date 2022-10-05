package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.post.PostResponse;
import surfy.comfy.entity.Bookmark;
import surfy.comfy.repository.BookmarkRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public List<PostResponse> getBookmarks(Long memberId){

        // 내가 북마크한 게시글들
        List<Bookmark> bookmarkList=bookmarkRepository.findAllByMember_Id(memberId);
        List<PostResponse> bookmarks=bookmarkList.stream()
                .map(b->new PostResponse(b))
                .collect(Collectors.toList());

        return bookmarks;
    }

}
