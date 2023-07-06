package com.wedding.serviceapi.common.jwtutil;

import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.common.vo.GuestBoardInfoVo;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.users.domain.Role;
import com.wedding.serviceapi.users.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GuestInfoJwtUtil extends JwtUtilBean<GuestBoardInfoVo>{

    private final GuestsRepository guestsRepository;
    private final UsersRepository usersRepository;
    private final BoardsRepository boardsRepository;

    private final String BOARDS_ID = "boardsId";
    private final String IS_REGISTERED = "isRegistered";

    @Override
    public String makeGuestInfoJwt(Long boardsId, Long usersId) {
        Key key = makeKey();
        Date now = new Date();

        Optional<Guests> optionalGuests = guestsRepository.findByUsersIdAndBoardsId(usersId, boardsId);

        if (optionalGuests.isEmpty()) {
            guestsRepository.save(
                    Guests.builder()
                            .users(usersRepository.getReferenceById(usersId))
                            .boards(boardsRepository.getReferenceById(boardsId))
                            .build()
            );
        }

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .claim(BOARDS_ID, boardsId)
                .claim(IS_REGISTERED, true)
                .signWith(key)
                .compact();
    }

    @Override
    public List<String> makeAccessTokenAndRefreshToken(Long userId, String userName, Long boardsId, Role role) {
        return null;
    }

    @Override
    public GuestBoardInfoVo decodeJwt(String jwt) {
        Key key = makeKey();

        Claims claims;
        try {
            claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 토큰 값입니다.");
        }

        Long boardsId = claims.get(BOARDS_ID, Long.class);
        Boolean isRegistered = claims.get(IS_REGISTERED, Boolean.class);

        return new GuestBoardInfoVo(boardsId, isRegistered);
    }
}
