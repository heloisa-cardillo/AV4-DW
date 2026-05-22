package com.autobots.automanager.adaptadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RepositorioUsuario repositorio;

    private Usuario obterPorNome(String nomeUsuario) {
        List<Usuario> usuarios = repositorio.findAll();
        for (Usuario usuario : usuarios) {
            for (Credencial credencial : usuario.getCredenciais()) {
                if (credencial instanceof CredencialUsuarioSenha) {
                    CredencialUsuarioSenha cus = (CredencialUsuarioSenha) credencial;
                    if (cus.getNomeUsuario().equals(nomeUsuario)) {
                        return usuario;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = obterPorNome(username);
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(usuario);
    }
}
