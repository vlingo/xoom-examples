<script>
	import { mdiChevronLeft, mdiChevronRight } from '@mdi/js';
	import { Card, CardActions, CardText, CardTitle, Row, Col } from 'svelte-materialify/src';
	import VlButton from './VlButton.svelte';

	export let title = "";
	export let nextLink = "";
	export let prevLink = "";

	export let isNextDisabled = false;
</script>

<Card class="vl-card pa-4 pa-md-8">
	<CardTitle class="pa-0">
		{title}
	</CardTitle>
	<CardText class="pa-0 mt-6 mb-6">
		<slot/>
	</CardText>
	<CardActions class="pa-0">
		<slot name="actions">
			<Row>
				{#if prevLink}
				<Col cols="6" class="text-left">
						<VlButton icon={mdiChevronLeft} text="Prev" href={prevLink} />
				</Col>
				{/if}
				{#if nextLink}
				<Col cols={prevLink ? 6 : 12} class="text-right">
					<VlButton icon={mdiChevronRight} text="Next" href={nextLink} disabled={isNextDisabled} />
				</Col>
				{/if}
			</Row>
		</slot>
	</CardActions>
</Card>

<style>
</style>