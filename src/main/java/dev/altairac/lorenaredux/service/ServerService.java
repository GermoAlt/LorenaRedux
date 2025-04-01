package dev.altairac.lorenaredux.service;

import dev.altairac.lorenaredux.model.Server;
import dev.altairac.lorenaredux.repository.ServerRepository;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServerService {
    private final ServerRepository serverRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public void updateServer(Guild guild) {
        Optional<Server> serverOptional = findServerById(guild.getIdLong());
        Server server = serverOptional.orElseGet(Server::new);
        serverRepository.save(server.initializeFromGuild(guild));
    }

    public Optional<Server> findServerById(Long id){
        return serverRepository.findById(id);
    }

    public List<Server> findAllServers(){
        return serverRepository.findAll();
    }

    public void updateServer(Server server) {
        serverRepository.save(server);
    }
}
