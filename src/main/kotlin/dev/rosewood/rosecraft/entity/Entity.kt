package dev.rosewood.rosecraft.entity

import dev.rosewood.rosecraft.registry.EntityRegistry

abstract class Entity {

    val entityId = EntityRegistry.idCounter.getAndIncrement()

}
