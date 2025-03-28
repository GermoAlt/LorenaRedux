package dev.altairac.lorenaredux.service;

import dev.altairac.lorenaredux.model.Server;
import dev.altairac.lorenaredux.repository.ServerRepository;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServerConfigService {
    private final ServerRepository serverRepository;

    @Autowired
    public ServerConfigService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public void updateServer(Guild guild) {
        Optional<Server> serverOptional = findServerById(guild.getIdLong());
        Server server = serverOptional.orElseGet(() -> new Server().initializeFromGuild(guild));
        serverRepository.save(server);
    }

    public Optional<Server> findServerById(Long id){
        return serverRepository.findById(id);
    }
}
