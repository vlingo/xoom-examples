<script>
	import { onMount } from 'svelte';
	import { theme, isMobile } from '../stores';
	import { Button, Icon, MaterialApp, AppBar, Container } from "svelte-materialify/src";
	import { mdiMenu, mdiWeatherNight, mdiWeatherSunny } from '@mdi/js';
	import SiteNavigation from '../components/SiteNavigation.svelte';
	export let segment;
	import { Api } from "../api";
	import { inventories, fleetcrews, flights } from "../stores";

	let sidenav = false;

	onMount(async () => {
		isMobile.check();
		$inventories = await Api.get("/aircrafts/");
		$flights = await Api.get("/flight-plannings/");
		$fleetcrews = await Api.get("/fleetcrew/aircrafts/");
	})
</script>

<svelte:window on:resize={isMobile.check} />

<div style="height: 100vh;">
<MaterialApp theme={$theme}>
	<AppBar fixed style="width:100%">
    	<div slot="icon">
    	  {#if $isMobile}
    	    <Button fab depressed on:click={() => (sidenav = !sidenav)} aria-label="Open Menu">
    	    	<Icon path={mdiMenu} />
    	    </Button>
    	  {/if}
		</div>
		<a href="inventory" slot="title" class="text--primary"><span style="color: var(--theme-text-primary);"> XOOM E2E Sys Airline Ops </span></a>
		<div style="flex-grow:1" />
    	<Button fab text on:click={() => theme.toggle()} aria-label="Toggle Theme">
    		<Icon path={$theme === "light" ? mdiWeatherNight : mdiWeatherSunny}/>
    	</Button>
	</AppBar>

	<SiteNavigation {segment} bind:mobile={$isMobile} bind:sidenav />

	<main class:navigation-enabled={!$isMobile}>
		<Container>
			<slot />
		</Container>
	</main>
</MaterialApp>
</div>

<style lang="scss" global>
	main {
	  padding-top: 5rem;
	}
	.navigation-enabled {
	  padding: 5rem 5rem 5rem calc(256px + 5rem);
	}

	.error-text {
		.s-input__details {
			color: inherit;
		}
	}

	a {
		text-decoration: none;
	}

	.s-card.vl-card {
		box-shadow: 0 0 64px 16px rgba(0, 0, 0, 0.075) !important;
		border-radius: 16px !important;
	}

	.s-input {
		margin-bottom: 1rem;
	}
	table, th, td {
		border-bottom: 1px solid lightgrey;
		border-collapse: collapse;
		padding: 0.5rem 1rem 0.75rem;
	}
	table {
		width: 100%;
		text-align: center;
	}
</style>
