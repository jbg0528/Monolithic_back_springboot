package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.post.PostResponse;
import surfy.comfy.entity.Bookmark;
import surfy.comfy.entity.Post;
import surfy.comfy.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final BookmarkService bookmarkService;

    @Transactional
    public List<PostResponse> getMyposts(Long memberId){

        // 내가 작성한 게시글들들
        List<Post> myPostList=postRepository.findAllByMember_Id(memberId);
        List<PostResponse> myPosts = myPostList.stream()
                .map(p -> new PostResponse(p))
                .collect(Collectors.toList());


        return myPosts;
    }


}
