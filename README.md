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

- [What is this even](#what-is-this-even)
- [Why its actually good](#why-its-actually-good)
- [How it actually works](#how-it-actually-works)
- [Quick Start](#-quick-start)
- [Troubleshooting](#its-broken-now-what)

———————————————————————————————————————————————————

## what is this even

its literally adding **4 custom cosmetic slots** right next to your inventory armor. terraria solved this problem like 13 years ago and minecraft still refuses to do it.
* put your high-stat armor (netherite) in real slots so u dont get 1-shot by a creeper
* put your stylish armor (leather, chainmail, or some weird headwear) in vanity slots
* look cute while keeping those defense stats

```
real slot: netherite chestplate (stats: +8 armor, +3 toughness)
cosmetic slot: leather jacket
result: u look like a biker, but gettin stats from netherite chestplate
```

———————————————————————————————————————————————————

## why its actually good

| Feature | Why it slaps |
|---------|-------------|
| **server-side magic** | vanilla players on server **will actually see** your cosmetic outfit, even if they dont have the mod installed!!! (ikr its crazy) |
| **no vanilla crashes** | safely hides custom slots from unmodded clients so they dont explode |
| **combat safe** | cosmetic armor **does not take durability damage** when u get hit. wear your rare sets without fear! |
| **creative mode sync** | custom slots r nicely aligned on the right of the survival inventory tab in creative |
| **death** | after death, your cosmetic armor doesnt drops |

<div style="display: flex; gap: 20px; align-items: center; margin-top: 25px;">
  <div style="flex: 1;">
    <strong>"vanilla view, combat & death proof":</strong>
    <p>server intercepts outgoing equipment packets and lies to vanilla players, telling them u r wearing vanity armor instead. plus, vanity items never lose durability in fights, and they behave exactly as they should on death without vanishing.</p>
  </div>
  <div>
    <img src="https://raw.githubusercontent.com/homkee1/TerrariaAccessories-Minecraft/assets/readme/gifs/2.gif" width="420" alt="Vanilla view, combat and death proof">
  </div>
</div>

<div style="display: flex; gap: 20px; align-items: center; margin-top: 25px;">
  <div style="flex: 1;">
    <strong>"creative tab interface":</strong>
    <p>no clunky UI overlaps. the vanity slots r perfectly integrated right next to your inventory model inside the creative mode survival tab.</p>
  </div>
  <div>
    <img src="https://raw.githubusercontent.com/homkee1/TerrariaAccessories-Minecraft/assets/readme/images/1.jpg" width="320" alt="Creative tab screenshot">
  </div>
</div>

———————————————————————————————————————————————————

## how it actually works

the sync engine uses a **dual-track system**:

```
                  [your cosmetic setup]
                            |
             +--------------+--------------+
             |                             |
      (local player)               (other players)
             |                             |
   reads from local              server intercepts packets
  visualArmor container          & swaps real armor with vanity
             |                             |
 renders via RenderState         renders vanity armor from packets!
```
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

compiled mod will be waiting for u

```
———————————————————————————————————————————————————
```
