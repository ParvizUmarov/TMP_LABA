package app.server.services;

import app.server.entity.TokenDB;
import app.server.repository.TokenRepository;

import java.util.Collection;
import java.util.List;

public class TokenCRUDService implements CRUDService<TokenDB>{

    private TokenRepository tokenRepository = new TokenRepository();

    @Override
    public Collection<TokenDB> getAll() {
        return List.of();
    }

    @Override
    public void create(TokenDB object) {

    }

    @Override
    public void update(TokenDB object) {

    }

    @Override
    public void delete(Integer id) {

    }

    public void deleteByToken(String token){
       tokenRepository.deleteByToken(token);
    }

    @Override
    public TokenDB get(String mail) {
        return tokenRepository.getUserToken(mail);
    }
}
