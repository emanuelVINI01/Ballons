# Balloons

Plugin de cosméticos para Spigot (1.8+) escrito em Kotlin. A ideia é simples: o jogador equipa um balão e ganha um efeito passivo enquanto ele estiver ativo — desde bônus de combate até efeitos puramente visuais.

## Efeitos implementados

O sistema de efeitos é baseado numa interface (`Effect`) com mais de 20 implementações prontas em `effect/impl/`:

- **Combate**: `DamageEffect`, `SmiteEffect`, `BowExtraEffect`, `BowReduceEffect`, `ReductionEffect`, `DodgeEffect`
- **Sobrevivência**: `HealthEffect`, `FallNegateEffect`, `SuffocateEffect`, `DurabilityEffect`
- **Economia/Progressão**: `MidasTouch`, `DropsEffect`, `ExpEffect`
- **Utilidade**: `FlyEffect`, `WarpEffect`, `CommandEffect`, `DisableGroup`
- **Diversão**: `BomberEffect`, `CakeMashEffect`, `StormEffect`, `SugarRushEffect`, `WebEffect`, `PotionEffect`, `BlessEffect`

Cada balão é modelado em `model/Balloon.kt` e gerenciado centralmente pelo `BalloonManager`, com comandos expostos via `BalloonCommand`.

## Stack

- Kotlin + Gradle (Shadow para fat-jar)
- `command-framework` (SaiintBrisson) para os comandos
- `configuration-injector` para carregar config/lang
- `item-nbt-api` para os itens customizados

## Integrações opcionais

`softdepend` com `EcoEnchants`, `EliteEnchantments` e `AdvancedEnchantments` — o plugin detecta essas APIs de encantamento se estiverem instaladas, mas funciona sem elas.
