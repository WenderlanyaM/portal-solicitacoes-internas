package info.bitsolucoes.portal.config;

import info.bitsolucoes.portal.model.Role;
import info.bitsolucoes.portal.model.User;
import info.bitsolucoes.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String htmlFormUser = authentication.getName();
        String htmlFormPassword = String.valueOf(authentication.getCredentials());

        User fetchedUser = userRepository.findUserWithRoleByName(htmlFormUser)
                .orElse(null);

        if ((fetchedUser != null) &&
                (fetchedUser.getId() > 0) &&
                passwordEncoder.matches(htmlFormPassword, fetchedUser.getPassword())) {

            return new UsernamePasswordAuthenticationToken(
                    fetchedUser.getName(), null, getGrantedAuthorities(fetchedUser.getRole())
            );
        } else {
            throw new AuthenticationCredentialsNotFoundException("Usuário ou senha incorretos!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Role role) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(
                new SimpleGrantedAuthority(role.getRole().toUpperCase())
        );
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authenticationType);
    }
}