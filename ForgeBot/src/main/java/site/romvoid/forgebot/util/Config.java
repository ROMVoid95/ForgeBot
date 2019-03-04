package site.romvoid.forgebot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

public class Config {

    private static final File configYML = new File("C:/Users/AJ/Downloads/Bots/ForgeBot 2.0/config.yml");
    private static final ObjectMapper configMapper = new ObjectMapper(new YAMLFactory());

    private static ConfigRoles LoadedRoles;

    public static Map<TextChannel, Map<Message, Map<Emote, Role>> > mappedRoles = new HashMap<>();

    public static void loadRoles(JDA jda) throws IOException {
        Config.LoadedRoles = configMapper.readValue(configYML, ConfigRoles.class);

        for(String channel : LoadedRoles.selfRole.keySet()){
            for(String message : LoadedRoles.selfRole.get(channel).keySet()){
                Map<Message, Map<Emote, Role>> messageMapMap = new HashMap<>();
                Message msg = jda.getTextChannelById(channel).getMessageById(message).complete();
                {
                    Map<Emote, Role> roleEmoteMap = new HashMap<>();
                    for(String role : LoadedRoles.selfRole.get(channel).get(message).keySet()){
                        roleEmoteMap.put(
                                jda.getEmoteById(LoadedRoles.selfRole.get(channel).get(message).get(role)),
                                jda.getRoleById(role)
                        );
                    }
                    messageMapMap.put(msg, roleEmoteMap);
                }
                mappedRoles.put(jda.getTextChannelById(channel), messageMapMap);
            }
        }
    }
    
    private static class ConfigRoles{
        public Map<String, Map<String, Map<String, String>> > selfRole;
    }
}
