<svelte:head>
	<title>Aircraft Monitoring</title>
</svelte:head>

<script>
	import CardForm from '../components/CardForm.svelte';
</script>

<CardForm
	title="Aircraft Monitoring"
	prevLink="air-traffic-control"
	isNextDisabled={false}
	dialogFormPresent={false}
>
	<table>
		<thead>
			<tr>
				<th>Flight</th>
				<th>Aircraft</th>
				<th>Status</th>
				<th>Location</th>
				<th>Actual Departure</th>
				<th>Estimated Arrival</th>
				<th>Actual Arrival</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>TK209</td>
				<td> Airbus A320-200</td>
				<td>In Flight</td>
				<td>0.1234343545-0.12312313434</td>
				<td>BAL - 12.12.2020 08:08:00</td>
				<td>IST - 12:12:2020 10:08:00</td>
				<td>IST - 12:12:2020 10:00:00</td>
			</tr>
		</tbody>
	</table>
</CardForm>

<style>
</style>

