# 🎭 Terraria Visual Armor (Vanity Slots)

> because running around in a generic netherite suit is so 2020. its time to get some actual drip.

<div style="display: flex; gap: 20px; align-items: center; justify-content: center; margin: 20px 0;">
  <img src="https://raw.githubusercontent.com/homkee1/TerrariaAccessories-Minecraft/assets/readme/gifs/1.gif" width="400" alt="Vanity slots showcase">
</div>

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.2%2B-red?style=flat-square)
![Modloader](https://img.shields.io/badge/Fabric-Loom-blue?style=flat-square)
![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)

———————————————————————————————————————————————————

## 📑 Quick Navigation

- [what is this even](#what-is-this-even)
- [why its actually good](#why-its-actually-good)
- [how it actually works](#how-it-actually-works)
- [Quick Start](#-quick-start)

———————————————————————————————————————————————————

## what is this even

its literally adding **4 custom cosmetic slots** right next to your inventory armor. terraria solved this like 13 years ago and minecraft still refuses to do it.
* put your high-stat armor (netherite) in real slots so u dont get 1-shot by a creeper
* put your stylish armor (leather jacket, chainmail, or some weird headwear) in vanity slots
* look cute while keeping those defense stats

```
real slot: netherite chestplate (stats: +8 armor, +3 toughness)
cosmetic slot: leather jacket
result: u look like a biker, but gettin stats from netherite chestplate
```

———————————————————————————————————————————————————

## why its actually good

* **server-side compatibility:** vanilla players on a server **will actually see** your cosmetic outfit, even if they dont have the mod installed (ikr its crazy).
* **no vanilla crashes:** safely hides custom slots from unmodded clients so they dont explode when opening inventories.
* **combat safe:** cosmetic items **dont take durability damage** when u get hit. wear your rare sets without fear of losing them.
* **creative mode support:** custom slots r nicely aligned on the right of the survival inventory tab in creative.
* **death proof:** after death, your cosmetic armor doesnt drop by default (u can config this).

<div style="display: flex; gap: 20px; align-items: center; margin-top: 25px;">
  <div style="flex: 1;">
    <strong>vanilla view & combat proof:</strong>
    <p>server intercepts outgoing equipment packets and lies to vanilla players, telling them u r wearing vanity armor instead. plus, vanity items never lose durability in fights.</p>
  </div>
  <div>
    <img src="https://raw.githubusercontent.com/homkee1/TerrariaAccessories-Minecraft/assets/readme/gifs/2.gif" width="420" alt="Vanilla view showcase">
  </div>
</div>

<div style="display: flex; gap: 20px; align-items: center; margin-top: 25px;">
  <div style="flex: 1;">
    <strong>creative tab interface:</strong>
    <p>no clunky UI overlaps. the vanity slots r integrated right next to your inventory model inside the creative mode survival tab.</p>
  </div>
  <div>
    <img src="https://raw.githubusercontent.com/homkee1/TerrariaAccessories-Minecraft/assets/readme/images/1.jpg" width="320" alt="Creative tab layout">
  </div>
</div>

———————————————————————————————————————————————————

## how it actually works

the logic is actually pretty straightforward:

1. **containers:** the mod adds a custom inventory container for your 4 vanity slots.
2. **local rendering:** locally, the client reads from this container and overrides your player model render state to show vanity armor instead of real armor.
3. **packet spoofing:** to make it work on servers, the server intercepts outgoing equipment packets (`SetEntityEquipPacket`). when sending updates to other players, it swaps your real armor with vanity. other players' clients think u r wearing vanity gear and render it normally.

———————————————————————————————————————————————————

## 🚀 Quick Start

### prerequisites
* **java 21** (minecraft 1.21.2+ demands it)
* **fabric loader**

```bash
git clone https://github.com/homkee1/Terraria1accessories.git
cd Terraria1accessories

./gradlew genSources
./gradlew build
```

compiled mod will be waiting for u in `build/libs/`
