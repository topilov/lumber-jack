package me.topilov.Utils

import me.kodysimpson.simpapi.heads.SkullCreator
import me.topilov.app
import org.bukkit.Bukkit
import org.bukkit.block.BlockFace
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class PetsAPI {

    val data = app.data
    val manager = app.manager

    fun reloadPetBoosters(player: Player) {
        val pets = app.activePets[player]?.split(":")

        app.petMoneyBooster[player] = 0.0
        app.petBlocksBooster[player] = 0.0
        app.petExpBooster[player] = 0.0
        app.petMobsBooster[player] = 0.0

        pets?.forEach{pet ->
            manager?.getPetsBooster()?.getConfigurationSection("pets.$pet.boostersPerLevel")?.getKeys(false)?.forEach { key ->

                app.getPetBoosterHashMap(key)?.put(player, app.getPetBoosterHashMap(key)?.get(player)!! +
                        manager.getPetsBooster()?.getDouble("pets.$pet.boostersPerLevel.$key")!! *
                        getPetLevel(player, pet.toInt()))
            }
        }
    }

    fun reloadPets(player: Player) {
        removePets(player)
        spawnPets(player)
    }

    fun spawnPets(player: Player) {

        val pets = app.activePets[player]?.split(":")
        val petsList = ArrayList<ArmorStand>()


        manager?.getPets()?.getConfigurationSection("pets")?.getKeys(false)?.forEach{currentPet ->

            if (pets?.contains(currentPet)!!) {

                val pet = player.world.spawnEntity(player.location, EntityType.ARMOR_STAND) as ArmorStand

                petsList.add(pet)

                val skullPet = SkullCreator.itemFromBase64(manager.getPets()?.getString("pets.$currentPet.helmetOwnerTexture"))

                pet.isCustomNameVisible = true
                pet.isVisible = false
                pet.customName = manager.getPets()?.getString("pets.$currentPet.name")?.replace("%level%", getPetLevel(player, currentPet.toInt()).toString())
                pet.setHelmet(skullPet)
                pet.setChestplate(null)
                pet.setLeggings(null)
                pet.setBoots(null)
                pet.setGravity(false)
                pet.isSmall = true
            }
        }

        app.armorStandsPets[player] = petsList
    }

    fun movePets(player: Player) {

        var i = 0

        app.armorStandsPets[player]?.forEach { pet ->

            i += 1

            when (i) {
                1 -> {
                    pet.teleport(player.location.clone().add(0.0, 0.5, 0.5))
                }
                2 -> {
                    pet.teleport(player.location.clone().add(-2.5, -0.25, -1.5))
                }
                3 -> {
                    pet.teleport(player.location.clone().add(-1.5, 0.0, 1.0))
                }
                4 -> {
                    pet.teleport(player.location.clone().add(-1.0, 1.0, -1.5))
                }
                5 -> {
                    pet.teleport(player.location.clone().add(-1.5, -0.25, -0.5))
                }
            }

            if (i >= app.armorStandsPets[player]?.size!! + 1) {
                i = 0
            }

        }
    }

    fun removePets(player: Player) {
        app.armorStandsPets[player]?.forEach{pet ->
            pet.remove()
        }
        app.armorStandsPets.remove(player)
    }

    fun getPetLevel(player: Player, pet: Int): Int {
        val pets = data!!.getString("pets", "pets", player.name)

        val petsSplit = pets.split(",")
        val level = petsSplit[pet - 1].split(":")[1]

        return level.toInt()
    }

    fun addPetLevel(player: Player, pet: Int, amount: Int) {
        val pets = data!!.getString("pets", "pets", player.name)

        val petsSplit = pets.split(",")
        var level = petsSplit[pet - 1].split(":")[1].toInt()

        level += amount

        val newPets = pets.replace(petsSplit[pet - 1], "$pet:$level")

        data.setString("pets", "pets", player.name, newPets)
    }


}