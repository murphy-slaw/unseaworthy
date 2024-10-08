# Unseaworthy

A mod that makes boats unsuitable for ocean voyages.

This mod makes vanilla minecraft boats unseaworthy. They operate normally on rivers and near shorelines, but as soon as
they enter an ocean biome, they begin to be tossed by the sea and eventually break apart.

## Features

- Boats deployed on water begin shaking and bouncing upon entering ocean biomes (any biome in the `c:is_ocean`  biome
  tag).
- You can optionally set a minimum water depth for boats to sink in tagged biomes.
- A configurable number of ticks later, the boat is wrecked.
- The % chance of being wrecked each interval is configurable.
- Wrecked boats can be configured to:
    - SINK: boat is pulled underwater and ejects passengers
    - BREAK: boat is broken and drops its item entity
    - DESTROY: boat is destroyed and drops a random number of planks and sticks

## Datapack Stuff

You can change which types of boat entities sink with the entity tag

```
unseaworthy/tags/entity_types/sinkable.json
``` 

You can change which biomes cause boats to sink with the biome tag

```
unseaworthy/tags/worldgen/biome/sinks_boats.json
```

## Why would you want that?

I needed a way to encourage players to build actual ships in a mod pack that is all about ocean voyages. Boats are too
good, so I made them worse.

## Acknowledgements

### Graphics

Icon: "Bailout" by Luis Prado
from <a href="https://thenounproject.com/browse/icons/term/bailout/" target="_blank" title="bailout Icons">Noun
Project</a> (CC BY 3.0)
