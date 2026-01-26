package loanfin.security.service;

import loanfin.entity.UserEntity;
import loanfin.repository.UserRepository;
import loanfin.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailHash)
            throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmailHash(emailHash)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with emailHash: " + emailHash));

        return new CustomUserDetails(user);
    }
}
