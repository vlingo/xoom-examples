import { mdiComment, mdiCommentOutline, mdiCube, mdiCubeOutline, mdiDatabase, mdiDatabaseOutline, mdiFolderDownload, mdiFolderDownloadOutline, mdiShape, mdiShapeOutline } from '@mdi/js';

export default [
	{
		text: 'Inventory',
		icon: mdiCommentOutline,
		openIcon: mdiComment,
		href: 'inventory'
	},
	{
		text: 'Flight Planning',
		icon: mdiShapeOutline,
		openIcon: mdiShape,
		href: 'flight-planning'
	},
	{
		text: 'Fleet Crew',
		icon: mdiDatabaseOutline,
		openIcon: mdiDatabase,
		href: 'fleet-crew'
	},
	{
		text: 'Airport Terminal',
		icon: mdiCubeOutline,
		openIcon: mdiCube,
		href: 'airport-terminal'
	},
	{
		text: 'Air Traffic Control',
		icon: mdiFolderDownloadOutline,
		openIcon: mdiFolderDownload,
		href: 'air-traffic-control'
	},
	{
		text: 'Aircraft Monitoring',
		icon: mdiFolderDownloadOutline,
		openIcon: mdiFolderDownload,
		href: 'aircraft-monitoring'
	},
];