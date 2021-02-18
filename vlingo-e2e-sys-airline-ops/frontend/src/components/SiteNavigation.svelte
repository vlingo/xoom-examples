<script>
	export let mobile;
	export let sidenav;
	import { NavigationDrawer, List, Overlay } from 'svelte-materialify/src';
	import NavItem from './NavItem.svelte';
	import routes from '../util/routes';

	export let segment;
</script>
  
<NavigationDrawer active={!mobile || sidenav} style="height:100vh;" fixed clipped borderless>
	<List nav dense>
		{#each routes as item}
			<NavItem {item} {segment}/>
		{/each}
	</List>
</NavigationDrawer>
<Overlay index="3" active={mobile && sidenav} on:click={() => (sidenav = false)} fadeOptions={{ duration: 250 }} />