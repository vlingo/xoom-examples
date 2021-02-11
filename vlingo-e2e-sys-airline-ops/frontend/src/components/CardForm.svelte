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
			<Row class="justify-center">
				{#if prevLink}
				<Col cols="auto">
						<VlButton icon={mdiChevronLeft} outlined text="Prev" href={prevLink} />
				</Col>
				{/if}
				{#if nextLink}
				<Col cols="auto" class="text-center">
					<VlButton icon={mdiChevronRight} outlined text="Next" href={nextLink} disabled={isNextDisabled} />
				</Col>
				{/if}
			</Row>
		</slot>
	</CardActions>
</Card>

<style>
</style>