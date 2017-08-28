package com.trafalcraft.SkyWars.util;

public enum Msg {
	Prefix("§bSkyWars §9§l> §r§b "),
	ERREUR("§4Erreur §l> §r§c "),
	NO_PERMISSIONS("§4Erreur §9§l> §r§bVous n'avez pas la permission de faire sa."),
	Command_Use("§4Erreur §l> §r§cutilisation de la commande: §6/sw "),
	
	//ERREUR
	String_to_int("La valeur du spawn doit etre numérique et comprise entre 0 et $MaxPlayers"),
    set_size_Wb("La valeur du worldborder doit etre numérique et comprise entre 1 et 32 767"),
    classes_no_exist("La classe $classe n'existe pas"),
	
	//setup
    Reload_Ok("Reload Ok!"),
    set_spawn_success("Le spawn $spawn a bien été configuré"),
    suppr_spawn_success("Le spawn $spawn a bien été supprimé, il reste: $rspawn spawn"),
    spawn_spec("Le spawn des spectateurs a bien été configuré"),
    set_wb("Le WorldBorder est bien configuré, le spectateur sera de la même taille"),
    setclasses("La classe $classe §ra bien été sauvegardé"),
    
    
    //jeux
    ARENA_START("Let the slaughter begin!");
    
    String value;

	private Msg(String value) {
		this.value = value;
        //set(value);
    }
    public String toString(){
    	return value;
    }
    //Main.icon
}
